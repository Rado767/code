#include <stdio.h>

#include "types.h"
#include "net.h"
#include "macros.h"
#include "options.h"

#include <errno.h>

/* This won't close (unless on error) but who cares */
void ws_thread(void * args) {
	struct ws_thread_args * a = args;
	WS_SAY("started, waiting for connection...\n");
	socket_t socket;
	if ((socket = accept_client(a->socket)) != -1) {
		WS_SAY("connected.\n");
	} else {
		WS_SAY_ "failed to connect client socket %i %i!\n", errno, a->socket);
		return -1;
	}
	
	/* Send packets! */
	struct ws_packet p;

	TIMED_LOOP

		p.blue_x = a->ws->blue->x;
		p.blue_y = a->ws->blue->y;
		p.blue_a = a->ws->blue->angle;

		p.yellow_x = a->ws->yellow->x;
		p.yellow_y = a->ws->yellow->y;
		p.yellow_a = a->ws->yellow->angle;

		p.ball_x = a->ws->ball->x;
		p.ball_y = a->ws->ball->y;
		
		WS_SAY_ "bx=%u by=%u ba=%u yx=%u yy=%u ya=%u bx=%u by=%u\r", p.blue_x, p.blue_y, p.blue_a, p.yellow_x, p.yellow_y, p.yellow_a, p.ball_x, p.ball_y);

		if (send(socket, &p, sizeof p, 0) == -1)
			WS_SAY("send failed\n");

	TIMED_LOOP_END
}