-- 课程管理系统数据库表结构

-- 课程表
CREATE TABLE course
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(100)   NOT NULL,
    description  TEXT,
    image_url    VARCHAR(255),
    credits      SMALLINT       NOT NULL,
    teacher_user_id   BIGINT         NOT NULL,
    start_date   TIMESTAMPTZ(0) NOT NULL,
    end_date     TIMESTAMPTZ(0) NOT NULL,
    status       SMALLINT       NOT NULL,
    created_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
);

-- 学生表
CREATE TABLE student
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(100)   NOT NULL,
    student_no   VARCHAR(50)    NOT NULL UNIQUE,
    faculty      VARCHAR(100)   NOT NULL,
    class_name   VARCHAR(50)    NOT NULL,
    user_id      BIGINT         NOT NULL,
    created_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
);

-- 教师表
CREATE TABLE teacher
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(100)   NOT NULL,
    faculty      VARCHAR(100)   NOT NULL,
    title        VARCHAR(50)    NOT NULL,
    user_id      BIGINT         NOT NULL,
    created_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
);

-- 选课表
CREATE TABLE enrollment
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_user_id   BIGINT         NOT NULL REFERENCES student(id),
    course_id    BIGINT         NOT NULL REFERENCES course(id),
    status       SMALLINT       NOT NULL,
    enroll_date  TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    created_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
);

-- 课程安排表
CREATE TABLE class_schedule
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id    BIGINT         NOT NULL REFERENCES course(id),
    classroom    VARCHAR(50)    NOT NULL,
    start_date   TIMESTAMPTZ(0) NOT NULL,
    end_date     TIMESTAMPTZ(0) NOT NULL,
    created_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
);

-- 索引
CREATE INDEX idx_course_teacher_user_id ON course(teacher_user_id);
CREATE INDEX idx_enrollment_student_user_id ON enrollment(student_user_id);
CREATE INDEX idx_enrollment_course_id ON enrollment(course_id);
CREATE INDEX idx_class_schedule_course_id ON class_schedule(course_id);
CREATE INDEX idx_student_user_id ON student(user_id);
CREATE INDEX idx_teacher_user_id ON teacher(user_id);

CREATE TABLE domain_event
(
    event_id    UUID           NOT NULL,
    listener_id VARCHAR(255)   NOT NULL,
    event_type  VARCHAR(255)   NOT NULL,
    payload     JSON           NOT NULL,
    created_at  TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),
    headers     JSON           NOT NULL,
    PRIMARY KEY (event_id, listener_id)
);
CREATE INDEX domain_event_idx_created_at ON domain_event (created_at);
