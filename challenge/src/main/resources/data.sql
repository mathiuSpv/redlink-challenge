INSERT INTO Currency(codigo, name)
VALUES ('ARS', 'Pesos Argentinos');

INSERT INTO Currency(codigo, name)
VALUES ('EUR', 'Euros');

INSERT INTO Currency(codigo, name)
VALUES ('USD', 'Dolares Americanos');


-- Generadas con GPT
INSERT INTO Transactions (transaction_id, user_id, amount, currency, status, created_at, bank_code, recipient_account)
VALUES (
   '550e8400-e29b-41d4-a716-446655440001',
   '1234',
   1400.00,
   'ARS',
   'APPROVED',
   '2024-09-15T10:30:00-03:00',
   'BANK123',
   'DE89370400440532013000'
);

INSERT INTO Transactions (transaction_id, user_id, amount, currency, status, created_at, bank_code, recipient_account)
VALUES (
   '550e8400-e29b-41d4-a716-446655440002',
   '1234',
   150.00,
   'USD',
   'APPROVED',
   '2024-09-15T11:15:00-03:00',
   'BANK456',
   'US64SVBKUS6S3300958879'
);

INSERT INTO Transactions (transaction_id, user_id, amount, currency, status, created_at, bank_code, recipient_account)
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    '1234',
    120.00,
    'EUR',
    'APPROVED',
    '2024-09-15T12:45:00-03:00',
    'BANK789',
    'FR1420041010050500013M02606'
);

INSERT INTO Transactions (transaction_id, user_id, amount, currency, status, created_at, bank_code, recipient_account)
VALUES (
    '550e8400-e29b-41d4-a716-446655440004',
    '4321',
    2500.00,
    'ARS',
    'APPROVED',
    '2024-09-15T14:20:00-03:00',
    'BANK321',
    'AR1234567890123456789012'
);