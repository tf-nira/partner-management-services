
CREATE TABLE pms.notification_history (
    id UUID PRIMARY KEY,
    partner_id VARCHAR(36) NOT NULL,
    notification_level INTEGER,
    first_notified_date TIMESTAMP,
    last_notified_date TIMESTAMP,
    notification_count INTEGER,
    resolved BOOLEAN DEFAULT FALSE,
    cr_by VARCHAR(256),
    cr_dtimes TIMESTAMP,
    upd_by VARCHAR(256),
    upd_dtimes TIMESTAMP
);

COMMENT ON TABLE notification_history IS 'Partner Balance Notification: Stores notification records for partners when their balance falls below configured thresholds, including notification level, count, and audit details.';
-- ddl-end --
COMMENT ON COLUMN notification_history.id IS 'ID: Unique identifier for the partner balance notification record.';
-- ddl-end --
COMMENT ON COLUMN notification_history.partner_id IS 'Partner ID: Unique identifier of the partner from the partner table.';
-- ddl-end --
COMMENT ON COLUMN notification_history.notification_level IS 'Notification Level: Indicates the threshold level at which the notification was triggered (e.g., Level 1, Level 2).';
-- ddl-end --
COMMENT ON COLUMN notification_history.first_notified_date IS 'First Notified Date: Timestamp when the partner was first notified about the balance threshold.';
-- ddl-end --
COMMENT ON COLUMN notification_history.last_notified_date IS 'Last Notified Date: Timestamp when the most recent notification was sent to the partner.';
-- ddl-end --
COMMENT ON COLUMN notification_history.notification_count IS 'Notification Count: Total number of notifications sent to the partner for the current balance threshold.';
-- ddl-end --
COMMENT ON COLUMN notification_history.resolved IS 'Resolved: Indicates whether the balance issue has been resolved and notifications are no longer required.';
-- ddl-end --
COMMENT ON COLUMN notification_history.cr_by IS 'Created By : ID or name of the user who create / insert record';
-- ddl-end --
COMMENT ON COLUMN notification_history.cr_dtimes IS 'Created DateTimestamp : Date and Timestamp when the record is created/inserted';
-- ddl-end --
COMMENT ON COLUMN notification_history.upd_by IS 'Updated By : ID or name of the user who update the record with new values';
-- ddl-end --
COMMENT ON COLUMN notification_history.upd_dtimes IS 'Updated DateTimestamp : Date and Timestamp when any of the fields in the record is updated with new values.';
-- ddl-end --