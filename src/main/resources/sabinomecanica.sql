-- 1. Tabela para registrar um endereço (Sem alterações estruturais)
CREATE TABLE endereco (
    id SERIAL PRIMARY KEY,
    rua VARCHAR(255) NOT NULL,
    numero VARCHAR(20),
    complemento VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL
);

-- 2. Tabela para informações de clientes
CREATE TABLE Cliente (
    cpf VARCHAR(11) PRIMARY KEY, -- CPF como chave primária
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    id_endereco INT,
    FOREIGN KEY (id_endereco) REFERENCES endereco(id)
);

-- 3. Tabela para informações sobre carros
CREATE TABLE Carro (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    placa VARCHAR(20) UNIQUE NOT NULL
);

-- 4. Tabela de Categorias para Serviços/Despesas (Antiga 'valores')
CREATE TABLE Categoria (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE
);
-- 5. Tabela para registrar serviços prestados (Ordem de Serviço)
CREATE TABLE Servico (
    id SERIAL PRIMARY KEY,
    data_ini DATE NOT NULL,
    data_fim DATE,
    data_garantia DATE,
    descricao TEXT,
    valor_total DECIMAL(10, 2), -- Valor total apurado dos itens e M.O.
    id_carro INT NOT NULL,
    cpf_cliente VARCHAR(11) NOT NULL,
    id_categoria INT, -- Ex: Revisão, Reparo de Motor, Funilaria

    FOREIGN KEY (id_carro) REFERENCES Carro(id),
    FOREIGN KEY (cpf_cliente) REFERENCES Cliente(cpf),
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id)
);

-- 6. Tabela para detalhar os Itens de um Serviço (Peças e Mão de Obra)
CREATE TABLE Item_Servico (
    id SERIAL PRIMARY KEY,
    id_servico INT NOT NULL,
    descricao VARCHAR(255) NOT NULL, -- Nome da peça ou da Mão de Obra
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10, 2) NOT NULL,

    FOREIGN KEY (id_servico) REFERENCES Servico(id)
);
-- 7. Tabela para registrar valores de entrada (Pagamentos de Serviços)
CREATE TABLE Movimento_Entrada (
    id SERIAL PRIMARY KEY,
    id_servico INT NOT NULL, -- O pagamento se refere a qual serviço
    data DATE NOT NULL,
    valor_pago DECIMAL(10, 2) NOT NULL, -- Valor efetivamente pago na data

    FOREIGN KEY (id_servico) REFERENCES Servico(id)
);

-- 8. Tabela para registrar Despesas (Valores de Saída)
CREATE TABLE Despesa (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL, -- Quando a despesa ocorreu/foi paga
    valor DECIMAL(10, 2) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    id_categoria INT, -- Ex: Aluguel, Salário, Compra de Peças, Luz

    FOREIGN KEY (id_categoria) REFERENCES Categoria(id)
);
-- Exemplo de consulta que calcula o saldo diário (Entrada - Saída)
SELECT
    t.data,
    SUM(CASE WHEN t.tipo = 'ENTRADA' THEN t.valor ELSE 0 END) AS Total_Entrada,
    SUM(CASE WHEN t.tipo = 'SAIDA' THEN t.valor ELSE 0 END) AS Total_Saida,
    (SUM(CASE WHEN t.tipo = 'ENTRADA' THEN t.valor ELSE 0 END) - SUM(CASE WHEN t.tipo = 'SAIDA' THEN t.valor ELSE 0 END)) AS Saldo_Dia
FROM (
    SELECT data, valor_pago AS valor, 'ENTRADA' AS tipo FROM Movimento_Entrada
    UNION ALL
    SELECT data, valor, 'SAIDA' AS tipo FROM Despesa
) AS t
GROUP BY t.data
ORDER BY t.data;