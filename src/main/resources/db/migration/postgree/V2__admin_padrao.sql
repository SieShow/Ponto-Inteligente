INSERT INTO empresa (cnpj, data_atualizacao, data_criacao, razao_social)
VALUES ('82198127000121', current_timestamp, current_timestamp, 'Calangus IT');

INSERT INTO funcionario (cpf, data_atualizacao, data_criacao, email, nome,
perfil, qtd_horas_almoco, qtd_horas_trabalho_dia, senha, valor_hora, empresa_id)
VALUES ('16248890935', current_timestamp, current_timestamp, 'admin@calangus.com', 'ADMIN', 'ROLE_ADMIN', NULL, NULL,
'$2a$04$AAd28Va3vMF.vSsMuthiNublwh8jKXymC/BMnyh7L5vsfZI0DLZO2', NULL, (SELECT id FROM empresa WHERE cnpj = '82198127000121'));