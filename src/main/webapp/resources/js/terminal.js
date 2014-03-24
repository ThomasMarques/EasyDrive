$(document).ready(function() {
	///global variables
	context="server";
	name_bakup="";

	//TODO : TO DELETE WHEN THE CSS IS DONE
	$(".ui-terminal-prompt").addClass("ui-terminal-prompt-server");

	//*****************TOOLS FUNCTION***********************
	// 
	//******************************************************

	function end_command(){
		$(".ui-terminal-content").first().children().each(function(){
			$(this).children().each(function(){
				if( $(this).is("span") && !$(this).hasClass("ui-terminal-command"))
					$(this).addClass("ui-terminal-prompt-"+context);
				else if( $(this).is("div") && $(this).html() == "is not a server command.")
					$(this).addClass("error");
			});
		});
		
		$('.ui-terminal-input', form).val("");
	}
	
	///add command-line to the front
	function add_elem(command, content, error){
		var elem = $("#elem-mess").html();
		elem = elem.replace("mycommand", command);
		elem = elem.replace("content", content);
		elem = elem.replace("display:none", "");
		if(error)
			elem = elem.replace("myclass", "error");
		$(".ui-terminal-content").append(elem);
	}

	function get_status(){
		var stat = "";

		$(".ui-fileupload-preview").each(function(){
			$(this).next().each(function(){
				stat = stat + $(this).html()+ '(';
			}).next().each(function(){
				stat = stat + $(this).html()+ ')  ';
			});
		});
		
		if(stat == "")
			stat = "Nothing to push."; 
		
		return stat;
	}

	function rem_last(){
		$(".ui-terminal-content").children(':last').remove();
	}

	function get_nb_file(){
		var i = 0;
		$(".ui-fileupload-preview").each(function(){
			i = i + 1;
		});
		return i;
	}

	function activate_form(){
		$("#form\\:terminal_input").attr("name", name_bakup);
	}

	function desactivate_form(){
		name_bakup = $("#form\\:terminal_input").attr("name");
		$("#form\\:terminal_input").attr("name", "");
	}

	//*****************COMMAND FUNCTION***********************
	// 
	//******************************************************

	function errorCommand(full_command){
		setTimeout(function() {
			add_elem(full_command, "is not a local command.", true);
		}, 20);	   	 		
	}

	function add(){
		//simulate click on primefaces file uploader
		$("#form-file\\:add-file_input").trigger("click");
		setTimeout(function() {
		}, 20);
	}

	function push(){
		setTimeout(function() {
			$(".ui-fileupload-upload").first().trigger("click");
			var nb_pushed = get_nb_file();
			var mess_push = "pushed";

			if(nb_pushed > 1)
				mess_push = " files " + mess_push;
			else
				mess_push = " file " + mess_push;

			add_elem("push", get_nb_file() + mess_push, false);
		}, 20);	   	 		
	}

	function remove(){
		setTimeout(function() {
			var nb_removed = get_nb_file();
			$(".ui-fileupload-cancel").first().trigger("click");

			var mess_rem = "removed";

			if(nb_removed > 1)
				mess_rem = " files " + mess_rem;
			else
				mess_rem = " file " + mess_rem;

			add_elem("remove", nb_removed + mess_rem, false);
		}, 20);	   	 		
	}

	function status(){
		setTimeout(function() {
			add_elem("status", get_status(), false);
		}, 20);	   	 		
	}

	function local(){
		if(context != "local"){
			setTimeout(function() {
				context="local";
				$(".ui-terminal-prompt").removeClass("ui-terminal-prompt-server");
				$(".ui-terminal-prompt").addClass("ui-terminal-prompt-local");
				$(".ui-button").trigger("click");
				desactivate_form();
			}, 30);	
		}
		else {
			setTimeout(function() {
				add_elem("local", "you are already in local context.", false);
			}, 20);	
		}
	}

	function server(){
		if(context != "server"){
			setTimeout(function() {
				context="server";
				$(".ui-terminal-prompt").removeClass("ui-terminal-prompt-local");
				$(".ui-terminal-prompt").addClass("ui-terminal-prompt-server");
				$(".ui-button").trigger("click");
				activate_form();
			}, 20);
		}
		else {
			setTimeout(function() {
				add_elem("server", "you are already in server context.", false);
			}, 20);	
		}
	}

	function pwd(){
		setTimeout(function() {
			add_elem("pwd", "local side.", false);
		}, 20);
	}

	function clear(){
		setTimeout(function() {
			$("#form\\:button_clear").trigger("click");
		}, 20);	   	 		
	}
	
	function help(){
		clear();
		setTimeout(function() {
			add_elem("", "local  : pwd, add, status, push, remove.", false);
			add_elem("", "server : ls, pwd", false);
			add_elem("", "both   : local, server, clear, help", false);
		}, 20);	   	 		
	}

	//**********************EVENT***************************
	// 
	//******************************************************

	//on file choosen by user
	$('#form-file input').change(function() {
		add_elem("add", get_status(), false);
	});
	
	$("#form\\:terminal").click(function(){
		$("#form\\:terminal_input").focus();
	});

	//event controller on keydow to create proxy before primefaces terminal
	$("#form").keydown(function(e) {
		//on enter key
		if( e.keyCode == 13 || e.which == 13) {
			var form = $(this);
			var formUrl=form.attr('action');
			var full_command = $('.ui-terminal-input', form).val();
			var command_id = full_command.split(" ")[0];

			e.preventDefault();

			// list of prioritar commands
			// local AND server side
			switch(command_id) {
			case "server": //change context to server
				server();
				return;
			case "local": //change context to local
				local();
				return;
			case "clear": //clear term
				clear();
				return;
			case "help": //clear term
				help();
				return;
			}

			//list of commands in local context
			if( context == "local" )  	
			{
				switch(command_id)
				{
				case "add": //add one or multiple files to a list
					add();
					break;
				case "push": //push all files of list on the server
					push();
					break;
				case "status": //print the list
					status();
					break;
				case "remove": //remove all files of the list
					remove();
					break;
				case "pwd": //get current context
					pwd();
					break;
				default: //error message
					errorCommand(full_command);
				break;
				}
			}
			
			setTimeout(function() {
				end_command();
			}, 30);

		}
	});
});