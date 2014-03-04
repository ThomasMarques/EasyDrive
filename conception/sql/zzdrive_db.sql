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
  `name` varchar(255) NOT NULL,
  `creation_date` date NOT NULL,
  `last_modification_date` date NOT NULL,
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

