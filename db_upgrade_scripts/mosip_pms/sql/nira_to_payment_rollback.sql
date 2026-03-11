DROP TABLE IF EXISTS pms.partner_prn;
DROP TABLE IF EXISTS pms.partner_current_balance;
DROP TABLE IF EXISTS pms.partner_payment_transactions;
DROP TABLE IF EXISTS pms.partner_balance_notification;


ALTER TABLE pms.partner DROP COLUMN requires_payment;
ALTER TABLE pms.partner_h DROP COLUMN requires_payment;