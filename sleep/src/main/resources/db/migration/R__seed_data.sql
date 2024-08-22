INSERT INTO users(id, external_id) VALUES (1, 'd54a761a-cbc3-451b-ac9a-b89898d7b6f9');

DO $$
DECLARE
    v_start_date TIMESTAMP;
    v_end_date TIMESTAMP;
    v_quality TEXT;
BEGIN
    FOR i IN 1..40 LOOP
        v_start_date := NOW() - (i * INTERVAL '1 day') - (INTERVAL '10 hours' + (random() * INTERVAL '4 hours'));

        v_end_date := v_start_date + (INTERVAL '5 hours' + (random() * INTERVAL '4 hours'));

        v_quality := CASE
                        WHEN random() < 0.33 THEN 'GOOD'::sleep_quality
                        WHEN random() < 0.66 THEN 'OK'::sleep_quality
                        ELSE 'BAD'::sleep_quality
                     END;

        INSERT INTO sleep(user_id, start_date, end_date, quality)
        VALUES (1, v_start_date, v_end_date, v_quality::sleep_quality);
    END LOOP;
END $$;