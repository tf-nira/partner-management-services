
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
COMMENT ON COLUMN partner_current_balance.partner_id IS 'Partner ID: Unique identifier of the partner from partner master table. Acts as primary key.';
COMMENT ON COLUMN partner_current_balance.balance IS 'Balance: Current available balance amount for the partner.';
COMMENT ON COLUMN pms.partner.cr_by IS 'Created By : ID or name of the user who create / insert record';
COMMENT ON COLUMN pms.partner.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
COMMENT ON COLUMN pms.partner.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
COMMENT ON COLUMN pms.partner.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';

