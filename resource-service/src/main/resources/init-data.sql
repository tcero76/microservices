INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb21', 'd215b5f8-0249-4dc5-89a3-51fd148cfb41', 500.00);

INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb24', 'd215b5f8-0249-4dc5-89a3-51fd148cfb41', 600.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb25', 'd215b5f8-0249-4dc5-89a3-51fd148cfb41', 100.00, 'DEBIT');

INSERT INTO payment.customer(customer_id, username, password, authorities)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb41', 'Leonardo', '{bcrypt}$2a$10$aZ.MWohJvZlhm31G6apwSe1JHGDc2Nt0VzPNz6NS4DvAu5NgAzz2i', 'USER');