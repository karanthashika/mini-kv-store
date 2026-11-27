import socket, sys
s = socket.create_connection(("localhost", 6380))
f = s.makefile("rw")
for cmd in sys.argv[1:]:
    f.write(cmd + "\n")
    f.flush()
    print(cmd, "->", f.readline().strip())
