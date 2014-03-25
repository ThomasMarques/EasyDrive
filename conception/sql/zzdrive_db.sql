-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mar 25 Mars 2014 à 23:46
-- Version du serveur: 5.6.12-log
-- Version de PHP: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `zzdrive_db`
--
CREATE DATABASE IF NOT EXISTS `zzdrive_db` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `zzdrive_db`;

-- --------------------------------------------------------

--
-- Structure de la table `back_file`
--

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

-- --------------------------------------------------------

--
-- Structure de la table `front_file`
--

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

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`id_user`, `login`, `password`, `salt`) VALUES
(1, 'admin', 'd9f3267977c85c3d0bfc0ae6406af0313c7313f8', '_?s>ONMVf4$yHU@%Sj$w');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `front_file`
--
ALTER TABLE `front_file`
  ADD CONSTRAINT `front_file_ibfk_2` FOREIGN KEY (`id_owner`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `front_file_ibfk_1` FOREIGN KEY (`id_back_file`) REFERENCES `back_file` (`id_back_file`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
