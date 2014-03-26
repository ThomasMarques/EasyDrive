CREATE TABLE IF NOT EXISTS `back_file` (
  `id_back_file` bigint(20) NOT NULL AUTO_INCREMENT,
  `hash` varchar(255) NOT NULL,
  `data` longblob NOT NULL,
  `size` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `creation_date` date NOT NULL,
  `last_modification_date` date NOT NULL,
  PRIMARY KEY (`id_back_file`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `front_file` (
  `id_front_file` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_back_file` bigint(20) NOT NULL,
  `id_owner` bigint(20) NOT NULL,
  `share` tinyint(1) NOT NULL DEFAULT '0',
  `abs_path` varchar(512) NOT NULL,
  PRIMARY KEY (`id_front_file`),
  KEY `id_back_file` (`id_back_file`),
  KEY `id_owner` (`id_owner`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

ALTER TABLE `front_file`
  ADD CONSTRAINT `front_file_ibfk_2` FOREIGN KEY (`id_owner`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `front_file_ibfk_1` FOREIGN KEY (`id_back_file`) REFERENCES `back_file` (`id_back_file`) ON DELETE CASCADE ON UPDATE CASCADE;
  
ALTER TABLE  `back_file` CHANGE  `hash`  `hash` VARCHAR( 255 ) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL ;