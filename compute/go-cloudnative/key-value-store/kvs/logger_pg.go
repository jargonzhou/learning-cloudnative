package kvs

import (
	"database/sql"
	"fmt"

	_ "github.com/lib/pq" // Anonymously import the driver package
	"go.uber.org/zap"
	"spike.com/key-value-store/config"
)

const (
	PgDriverName = "postgres"
	// PgDB         = "kvs"
	PgSchema = "public"
	PgTable  = "transactions"
)

type PgTransactionLogger struct {
	events chan<- Event
	errors <-chan error

	db *sql.DB
}

func (l *PgTransactionLogger) WritePut(key, value string) {
	l.events <- Event{EventType: EventPut, Key: key, Value: value}
}

func (l *PgTransactionLogger) WriteDelete(key string) {
	l.events <- Event{EventType: EventDelete, Key: key}
}

func (l *PgTransactionLogger) Err() <-chan error {
	return l.errors
}

func (l *PgTransactionLogger) ReadEvents() (<-chan Event, <-chan error) {
	outEvent := make(chan Event)
	outError := make(chan error, 1)

	go func() {
		defer close(outEvent)
		defer close(outError)

		query := "SELECT sequence, event_type, key, value FROM " + PgTable + " ORDER BY sequence"
		rows, err := l.db.Query(query)
		if err != nil {
			outError <- fmt.Errorf("sql query error: %w", err)
			return
		}

		defer rows.Close()
		e := Event{}
		for rows.Next() {
			err = rows.Scan(&e.Sequence, &e.EventType, &e.Key, &e.Value)
			if err != nil {
				outError <- fmt.Errorf("error reading row: %w", err)
				return
			}

			outEvent <- e
		}
	}()

	return outEvent, outError
}

func (l *PgTransactionLogger) Run() {
	events := make(chan Event, 16)
	l.events = events

	errors := make(chan error, 1)
	l.errors = errors

	go func() {
		query := `INSERT INTO ` + PgTable + `(event_type, key, value) VALUES ($1, $2, $3)`

		for e := range events {
			_, err := l.db.Exec(query, e.EventType, e.Key, e.Value)
			if err != nil {
				errors <- err
			}
		}

	}()
}

type PgDBParams struct {
	host     string
	dbname   string
	user     string
	password string
}

func NewPgTransactionLogger(host, dbname, user, password string) (TransactionLogger, error) {
	return newPgTransactionLogger(PgDBParams{
		host:     host,
		dbname:   dbname,
		user:     user,
		password: password,
	})
}

func newPgTransactionLogger(pgDBParams PgDBParams) (TransactionLogger, error) {
	connStr := fmt.Sprintf("host=%s dbname=%s user=%s password=%s sslmode=disable",
		pgDBParams.host, pgDBParams.dbname, pgDBParams.user, pgDBParams.password)

	db, err := sql.Open(PgDriverName, connStr)
	if err != nil {
		config.Zaplog.Error("failed to open db", zap.Error(err))
		return nil, fmt.Errorf("failed to open db: %w", err)
	}

	err = db.Ping()
	if err != nil {
		config.Zaplog.Error("failed to open db connection", zap.Error(err))
		return nil, fmt.Errorf("failed to open db connection: %w", err)
	}

	logger := &PgTransactionLogger{db: db}
	exists, err := logger.verifyTableExists()
	if err != nil {
		config.Zaplog.Error("failed to verify tables exists", zap.Error(err))
		return nil, fmt.Errorf("failed to verify tables exists: %w", err)
	}
	if !exists {
		if err = logger.createTable(); err != nil {
			config.Zaplog.Error("failed to create table", zap.Error(err))
			return nil, fmt.Errorf("failed to create table: %w", err)
		}
	}

	config.Zaplog.Info("Create PgTransactionLogger done")
	return logger, nil
}

func (l *PgTransactionLogger) verifyTableExists() (bool, error) {

	var result string

	rows, err := l.db.Query(fmt.Sprintf("SELECT to_regclass('%s.%s');", PgSchema, PgTable))
	defer rows.Close()
	if err != nil {
		return false, err
	}

	for rows.Next() && result != PgTable {
		rows.Scan(&result)
	}

	return result == PgTable, rows.Err()
}

func (l *PgTransactionLogger) createTable() error {
	var err error

	createQuery := `CREATE TABLE ` + PgTable + ` (
		sequence      BIGSERIAL PRIMARY KEY,
		event_type    SMALLINT,
		key 		  TEXT,
		value         TEXT
	  );`

	_, err = l.db.Exec(createQuery)
	if err != nil {
		return err
	}

	return nil
}
