package repaso.dto;

import java.sql.Timestamp;

public record Actor (int actor_id, String first_name, String last_name, Timestamp last_update) {
	public Actor(int actor_id, String first_name, String last_name) {
        this(actor_id, first_name, last_name, null);
    }
}
