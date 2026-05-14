
CREATE TABLE partner_prn (
    partner_id   character varying(36) NOT NULL,
	partner_name character varying(128) NOT NULL,
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
COMMENT ON COLUMN pms.partner_prn.partner_id IS 'Partner ID: Unique identifier of the partner from partner table.';
COMMENT ON COLUMN pms.partner_prn.prn IS 'PRN: Payment Reference Number generated for the partner transaction.';
COMMENT ON COLUMN pms.partner_prn.status IS 'Status: Current status of the PRN transaction (e.g., GENERATED, VALIDATED-PAID, FAILED, SETTLED).';
COMMENT ON COLUMN pms.partner_prn.amount IS 'Amount: Transaction amount associated with the PRN.';
COMMENT ON COLUMN pms.partner_prn.service_code IS 'Service Code: Code representing the service for which the payment is made.';
COMMENT ON COLUMN pms.partner_prn.remarks IS 'Remarks: Additional remarks or description related to the PRN transaction.';
COMMENT ON COLUMN pms.partner_prn.cr_by IS 'Created By : ID or name of the user who create / insert record';
COMMENT ON COLUMN pms.partner_prn.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
COMMENT ON COLUMN pms.partner_prn.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
COMMENT ON COLUMN pms.partner_prn.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
