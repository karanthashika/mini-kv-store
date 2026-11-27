# mini-kv-store

A Redis-like key-value store in Java with persistence, replication,
and Raft-based leader election.

## Status
- [x] Stage 1: TCP server with SET / GET / DELETE
- [x] Stage 2: Write-ahead log with fsync, crash recovery by replay
- [ ] Stage 3: Replication across 3 nodes
- [ ] Stage 4: Leader election (Raft)

## Run
    mvn clean compile
    java -cp target/classes kv.Main

## Try it
    python scripts/kv.py "SET name Rahul" "GET name"

## Persistence demo
1. Run the server and SET a key.
2. Stop the server (Ctrl+C) and start it again.
3. GET the key. It's still there, rebuilt from `data/wal.log`.
