#!/bin/bash
adb pull data/data/com.samknows.fcc/databases/sk.db
sqlite3 sk.db
