DROP TABLE IF EXISTS pms.partner_prn;
DROP TABLE IF EXISTS pms.partner_current_balance;
DROP TABLE IF EXISTS pms.partner_payment_transactions;
DROP TABLE IF EXISTS pms.notification_history;
DROP TABLE IF EXISTS pms.partners_transaction;


-- partner table
ALTER TABLE pms.partner 
DROP COLUMN IF EXISTS requires_payment,
ADD COLUMN IF NOT EXISTS partnerAuthType character varying(128),
ADD COLUMN IF NOT EXISTS partnerGroup character varying(128);

-- partner_h table
ALTER TABLE pms.partner_h 
DROP COLUMN IF EXISTS requires_payment,
ADD COLUMN IF NOT EXISTS partnerAuthType character varying(128),
ADD COLUMN IF NOT EXISTS partnerGroup character varying(128);