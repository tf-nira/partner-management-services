
CREATE TABLE partner_payment_transactions (
    transaction_id character varying(36) NOT NULL,
    partner_id     character varying(36) NOT NULL,
    entry_type     character varying(36) NOT NULL,
    amount         numeric NOT NULL,
    source_system  character varying(256) NOT NULL,
    description    character varying(256) NOT NULL,
    log_dtimes     timestamp NOT NULL,
    cr_by          character varying(256) NOT NULL,
    cr_dtimes      timestamp NOT NULL,
    upd_by         character varying(256),
    upd_dtimes     timestamp,
	CONSTRAINT pk_part PRIMARY KEY (partner_id)
);

COMMENT ON TABLE partner_payment_transactions IS 'Partner Payment Transactions: Stores all credit and debit transactions for partner accounts including source, amount, and audit details.';
COMMENT ON COLUMN partner_payment_transactions.transaction_id IS 'Transaction ID: Unique identifier for the payment transaction.';
COMMENT ON COLUMN partner_payment_transactions.partner_id IS 'Partner ID: Unique identifier of the partner from partner table.';
COMMENT ON COLUMN partner_payment_transactions.entry_type IS 'Entry Type: Indicates transaction nature such as CREDIT or DEBIT.';
COMMENT ON COLUMN partner_payment_transactions.amount IS 'Amount: Transaction amount credited or debited to the partner account.';
COMMENT ON COLUMN partner_payment_transactions.source_system IS 'Source System: System from which the transaction originated (e.g., IDA, PMS).';
COMMENT ON COLUMN partner_payment_transactions.description IS 'Description: Description or remarks for the payment transaction.';
COMMENT ON COLUMN partner_payment_transactions.log_dtimes IS 'Log DateTimestamp: Date and timestamp when the transaction event occurred.';
COMMENT ON COLUMN pms.partner.cr_by IS 'Created By : ID or name of the user who create / insert record';
COMMENT ON COLUMN pms.partner.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
COMMENT ON COLUMN pms.partner.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
COMMENT ON COLUMN pms.partner.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';