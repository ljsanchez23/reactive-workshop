package co.com.nequi.r2dbc.helper;

public class Constants {
    public final static String INSERT_USER_QUERY = """
        INSERT INTO users (id, email, first_name, last_name, avatar_url)
        VALUES (:#{#user.id}, :#{#user.email}, :#{#user.firstName}, :#{#user.lastName}, :#{#user.avatarUrl})
        RETURNING *
    """;
}
