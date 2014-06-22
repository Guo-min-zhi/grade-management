ALTER TABLE `certificate_type`
ADD COLUMN `upload_type`  tinyint NULL AFTER `formula`;

ALTER TABLE `certificate_score`
ADD COLUMN `oral_score`  float NULL AFTER `grade_final`,
ADD COLUMN `written_score`  float NULL AFTER `oral_score`;

ALTER TABLE `certificate_score`
ADD COLUMN `grade_status`  tinyint NULL AFTER `written_score`;