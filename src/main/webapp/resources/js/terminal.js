$(document).ready(function() {
	///global variables
	context="login";
	name_bakup_terminal="";
	user="";
	password="";
	command_save="";
    loop=false;
    rebond=false;
    last_command="";

	//TODO : TO DELETE WHEN THE CSS IS DONE
	$(".ui-terminal-prompt").addClass("ui-terminal-prompt-login");
	desactivate_form_terminal();
    desactivate_form_download();

    //*****************GRAPHICAL FUNCTION***********************
    //
    //******************************************************

    function start() {

    }

    function stop() {

    }

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
		$('.ui-terminal-command').last.val(last_command);
        last_command="";
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

	function activate_form_terminal(){
		$("#form\\:terminal_input").attr("name", name_bakup_terminal);
	}

	function desactivate_form_terminal(){
		name_bakup_terminal = $("#form\\:terminal_input").attr("name");
		$("#form\\:terminal_input").attr("name", "");
	}

    function activate_form_download(){
        $("#form-file-download\\:downloadLink").removeAttr("disabled");
    }

    function desactivate_form_download(){
        $("#form-file-download\\:downloadLink").attr("disabled", "disabled");
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
		//setTimeout(function() {
		//}, 20);
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
				$("#form\\:button_clear").trigger("click");
				desactivate_form_terminal();
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
				$("#form\\:button_clear").trigger("click");
				activate_form_terminal();
			}, 20);
		}
		else {
			setTimeout(function() {
				add_elem("server", "you are already in server context.", false);
			}, 20);	
		}
	}

	function login(){
		$(".ui-terminal-prompt").removeClass("ui-terminal-prompt-server");
		$(".ui-terminal-prompt").addClass("ui-terminal-prompt-login");
	}
	
	function pwd(){
		setTimeout(function() {
			add_elem("pwd", "local side.", false);
		}, 20);
	}

	function clear(){
		//setTimeout(function() {
			$("#form\\:button_clear").trigger("click");
		//}, 20);	   	 		
	}
	
	function help(){
		clear();
		setTimeout(function() {
			add_elem("help", "local  : pwd, add, status, push, remove.<br/>server : ls, pwd<br/>both   : local, server, clear, help", false);
		}, 20);	   	 		
	}

    function download(){
        activate_form_download();
        $("#form-file-download\\:downloadLink").trigger("click");
        desactivate_form_download();
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

	base_cpt = 0;
	$(document).bind("DOMSubtreeModified", function(evt) {

		if(context == "server" && !rebond){
            var terminalContent = $(".ui-terminal-content");
			var respond = terminalContent.children(':last').children(':last').html();
			var new_cpt = terminalContent.children().size();
            var code;

			if( new_cpt != base_cpt )
			{
				base_cpt = new_cpt;
				$("#form\\:terminal_input").removeAttr('disabled');
		    	$("#form\\:terminal").trigger("click");
			}

            if(respond == null || respond == "" || respond.length < 44)
                return;

            var code = respond.substring(27, 30);

            if(code == "200" && respond.substring(37, 44) == "Welcome")
	    	{
				base_cpt = 0;
				context="server";
		    	clear();
	    		add_elem("connection", respond, false);
	    	}
		    else if( code == "401")
	    	{
		    	base_cpt = 0;

		    	desactivate_form_terminal();
                $("#form\\:terminal_input").attr('disabled', 'disabled');
		    	context="login";
		    	clear();
				$(".ui-terminal-prompt").removeClass("ui-terminal-prompt-server");
				$(".ui-terminal-prompt").addClass("ui-terminal-prompt-login");
				$("#form\\:button_clear").trigger("click");
				add_elem("connection", respond, false);

                //TODO : change this fix
                setTimeout(function() {
                    location.reload();
                }, 2000);
	    	}
            else if(code == "200" && respond.substring(39, 50) == "Downloading")
            {
                //TODO : remove this fix
                //problem probably with the cpt
                base_cpt = 0;
                clear();

                rebond = true;
                download();
            }

			if( respond != null && new_cpt == base_cpt)
				setTimeout(function() {
					end_command();
				}, 30);
        }
	});
	
	//event controller on keydown to create proxy before primefaces terminal
	$("#form").keydown(function(e) {
		//on enter key
        rebond = false;
        if( e.keyCode == 13 || e.which == 13) {
			var form = $(this);
			var formUrl=form.attr('action');
			var full_command = $('.ui-terminal-input', form).val();
			var command_id = full_command.split(" ")[0];
			command_save = command_id;
			e.preventDefault();
            command_save = full_command;

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
			
			if( context == "server" )  	
			{
				$("#form\\:terminal_input").attr("disabled", "disabled");
			}
			if( context == "login" )  	
			{
				//TODO : some verif on login
				login = command_id;
				context = password;
				$("#form\\:terminal_input").attr("type", "password");
				context="password";
				
				setTimeout(function() {
					$(".ui-terminal-prompt").removeClass("ui-terminal-prompt-login");
					$(".ui-terminal-prompt").addClass("ui-terminal-prompt-password");
					$("#form\\:button_clear").trigger("click");
				}, 30);	
				
			}
			else if( context == "password" )  	
			{
                rebond = false;
				password = command_id;
				$(".ui-terminal-prompt").removeClass("ui-terminal-prompt-password");
				server();
				context = "server";
				$("#form\\:terminal_input").attr("type", "default");
				
				setTimeout(function() {
					$("#form\\:terminal_input").val("login "+login+" "+password);
					var press = jQuery.Event("keydown");
					press.which = 13;
					$("#form\\:terminal_input").trigger(press);	
					add_elem("connection", "contacting server", false);
					$("#form\\:terminal_input").attr("disabled", "disabled");
					$("#form\\:terminal_input").val("");
				}, 30);
			}
			else if( context == "local" )  	
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
                $("#form\\:terminal_input").val("");
				setTimeout(function() {
						end_command();
				}, 30);
			}
		}
	});
});