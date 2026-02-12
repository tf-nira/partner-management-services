
-- object: pms.partner_prn | type: TABLE --
-- DROP TABLE IF EXISTS pms.partner_prn CASCADE;
CREATE TABLE partner_prn (
    partner_id   character varying(36) NOT NULL,
    prn          character varying(36) NOT NULL,
    status       character varying(36) NOT NULL,
    amount       numeric NOT NULL,
    service_code character varying(36) NOT NULL,
    remarks      character varying(256) NOT NULL,
    cr_by        character varying(256) NOT NULL,
    cr_dtimes    timestamp NOT NULL,
    upd_by       character varying(256),
    upd_dtimes   timestamp,
	CONSTRAINT pk_part PRIMARY KEY (partner_id, prn)
);


COMMENT ON TABLE partner_prn IS 'Partner PRN: Stores PRN transaction and service details associated with partners including amount, service code, and processing status.';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.partner_id IS 'Partner ID: Unique identifier of the partner from partner table.';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.prn IS 'PRN: Payment Reference Number generated for the partner transaction.';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.status IS 'Status: Current status of the PRN transaction (e.g., GENERATED, VALIDATED-PAID, FAILED, SETTLED).';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.amount IS 'Amount: Transaction amount associated with the PRN.';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.service_code IS 'Service Code: Code representing the service for which the payment is made.';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.remarks IS 'Remarks: Additional remarks or description related to the PRN transaction.';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN pms.partner_prn.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --



-- object: pms.partner_current_balance | type: TABLE --
-- DROP TABLE IF EXISTS pms.partner_current_balance CASCADE;
CREATE TABLE partner_current_balance (
    partner_id character varying(36) NOT NULL,
    balance     numeric NOT NULL,
    cr_by       character varying(256) NOT NULL,
    cr_dtimes   timestamp NOT NULL,
    upd_by      character varying(256), 
    upd_dtimes  timestamp,
	CONSTRAINT pk_part PRIMARY KEY (partner_id)
);

COMMENT ON TABLE partner_current_balance IS 'Partner Current Balance: Stores the current wallet or account balance of each partner for service transactions.';
-- ddl-end --
COMMENT ON COLUMN partner_current_balance.partner_id IS 'Partner ID: Unique identifier of the partner from partner master table. Acts as primary key.';
-- ddl-end --
COMMENT ON COLUMN partner_current_balance.balance IS 'Balance: Current available balance amount for the partner.';
-- ddl-end --
COMMENT ON COLUMN pms.partner.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN pms.partner.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN pms.partner.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN pms.partner.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --



-- object: pms.partner_payment_transactions | type: TABLE --
-- DROP TABLE IF EXISTS pms.partner_payment_transactions CASCADE;
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
	CONSTRAINT pk_part PRIMARY KEY (transaction_id)
);

COMMENT ON TABLE partner_payment_transactions IS 'Partner Payment Transactions: Stores all credit and debit transactions for partner accounts including source, amount, and audit details.';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.transaction_id IS 'Transaction ID: Unique identifier for the payment transaction.';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.partner_id IS 'Partner ID: Unique identifier of the partner from partner table.';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.entry_type IS 'Entry Type: Indicates transaction nature such as CREDIT or DEBIT.';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.amount IS 'Amount: Transaction amount credited or debited to the partner account.';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.source_system IS 'Source System: System from which the transaction originated (e.g., IDA, PMS).';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.description IS 'Description: Description or remarks for the payment transaction.';
-- ddl-end --
COMMENT ON COLUMN partner_payment_transactions.log_dtimes IS 'Log DateTimestamp: Date and timestamp when the transaction event occurred.';
-- ddl-end --
COMMENT ON COLUMN pms.partner.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN pms.partner.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN pms.partner.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN pms.partner.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --



-- object: pms.partner | type: TABLE --
ALTER TABLE pms.partner ADD COLUMN requires_payment boolean NOT NULL;

-- object: pms.partner_h | type: TABLE --
ALTER TABLE pms.partner_h ADD COLUMN requires_payment boolean NOT NULL;