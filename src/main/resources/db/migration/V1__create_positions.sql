CREATE TABLE positions (
  id BIGSERIAL PRIMARY KEY,
  trade_id VARCHAR(100) NOT NULL,
  desk VARCHAR(100) NOT NULL,
  product_family VARCHAR(100) NOT NULL,
  product_type VARCHAR(20) NOT NULL CHECK (product_type IN ('BOND','FUTURE')),
  quantity NUMERIC(20,6) NOT NULL,
  face_value NUMERIC(20,6),
  contract_multiplier NUMERIC(20,6),
  previous_price NUMERIC(20,6) NOT NULL,
  current_price NUMERIC(20,6) NOT NULL,
  CONSTRAINT chk_product_input CHECK ((product_type='BOND' AND face_value IS NOT NULL) OR (product_type='FUTURE' AND contract_multiplier IS NOT NULL))
);
