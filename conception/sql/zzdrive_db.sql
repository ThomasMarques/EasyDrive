-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mer 26 Février 2014 à 15:24
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
  `id` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `data` longblob NOT NULL,
  `size` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `front_file`
--

CREATE TABLE IF NOT EXISTS `front_file` (
  `id` varchar(255) NOT NULL,
  `id_blob` varchar(255) NOT NULL,
  `id_owner` varchar(255) NOT NULL,
  `share` tinyint(1) NOT NULL DEFAULT '0',
  `abs_path` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
