CREATE TABLE `workshop-primefaces`.`Empresa` (
  `id` BIGINT UNSIGNED NOT NULL DEFAULT 0,
  `cnpj` VARCHAR(18) NOT NULL DEFAULT '',
  `data_fundacao` DATE NOT NULL DEFAULT 0,
  `nome_fantasia` VARCHAR(80) NOT NULL DEFAULT '',
  `razao_social` VARCHAR(120) NOT NULL DEFAULT '',
  `tipo` VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY(`id`)
)
ENGINE = InnoDB;