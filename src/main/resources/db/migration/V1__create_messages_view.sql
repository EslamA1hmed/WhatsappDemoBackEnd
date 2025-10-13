DROP VIEW IF EXISTS all_messages_view;

CREATE OR REPLACE VIEW all_messages_view AS
SELECT
    m.id AS id,
    m.message_id AS message_id,
    'SENT' AS direction,
    m.status AS status,
    m.`to` AS contact,
    m.type AS type,
    m.text_body AS text_body,
    m.template_name AS template_name,
    m.template_body AS template_body,
    m.template_header AS template_header,
    m.template_footer AS template_footer,
    m.media_id AS media_id,
    m.media_url AS media_url,
    m.mime_type AS mime_type,
    m.caption AS caption,
    m.filename AS filename,
    b.id AS button_id,
    b.type AS button_type,
    b.text AS button_text,
    b.payload AS button_payload,
    b.url AS button_url,
    b.phone_number AS button_phone,
    m.created_at AS created_at,
    m.context_message_id AS context_message_id,
    NULL AS context_from
FROM messages_outgoing m
LEFT JOIN message_buttons b 
    ON b.message_id = m.id  

UNION ALL

SELECT
    i.id AS id,
    i.message_id AS message_id,
    'RECEIVED' AS direction,
    'RECEIVED' AS status,
    i.`from_` AS contact,
    i.type AS type,
    i.text_body AS text_body,
    NULL AS template_name,
    NULL AS template_body,
    NULL AS template_header,
    NULL AS template_footer,
    i.media_id AS media_id,
    NULL AS media_url,
    i.mime_type AS mime_type,
    i.caption AS caption,
    i.filename AS filename,
    NULL AS button_id,
    NULL AS button_type,
    NULL AS button_text,
    NULL AS button_payload,
    NULL AS button_url,
    NULL AS button_phone,
    i.received_at AS created_at,
    i.context_message_id AS context_message_id,
    i.context_from AS context_from
FROM messages_incoming i;
