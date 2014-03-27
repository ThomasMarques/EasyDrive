-- User data
INSERT INTO `user` (`id_user`, `login`, `password`, `salt`) VALUES
(1, 'admin', 'd9f3267977c85c3d0bfc0ae6406af0313c7313f8', '_?s>ONMVf4$yHU@%Sj$w');
-- End User data

-- Back file data
INSERT INTO `back_file` (`id_back_file`, `hash`, `data`, `size`, `name`, `creation_date`, `last_modification_date`) VALUES
(1, NULL, NULL, 0, '', NULL, NULL),
(2, NULL, NULL, 0, 'home', NULL, NULL),
(3, NULL, NULL, 0, 'share', NULL, NULL);
-- End Back file data

-- Front file data
INSERT INTO `front_file` (`id_front_file`, `id_back_file`, `id_owner`, `share`, `abs_path`) VALUES
(1, 1, 1, 0, ''),
(2, 2, 1, 0, '/'),
(3, 3, 1, 0, '/');
-- End Front file data