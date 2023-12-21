# Tokens

For now, all tokens that are used by the Burrow Studios API are JWTs.

## Identity token

An identity token is used to authenticate the bearer as a legitimate application and to request a session token from the
API (handled by Shelly). It does not contain any information on application scopes or intents, as they are managed
internally. This means that a login request to the API is stateful until the client receives a
[session token](#session-token).

Identity tokens have a long-lived lifecycle and are only renewed when they are manually invalidated or expire as
expected. Later versions of this API will likely implement some sort of security measures by which identity tokens must
be refreshed.

#### JWT Structure

#### Header

| Field | Type    | Description                                                  |
|-------|---------|--------------------------------------------------------------|
| typ   | string  | Token type ("JWT")                                           |
| alg   | string  | A hint to what algorithm should be used to verify the token. |

#### Payload

| Field | Type    | Description                                              |
|-------|---------|----------------------------------------------------------|
| iss   | string  | Token issuer. Typically "Shelly".                        |
| sub   | turtle  | Token subject. This is the unique id of the application. |
| exp   | numDate | A timestamp that this token must not be used _after_.    |
| ?nbf  | numDate | A timestamp that this token must not be used _before_.   |
| jti   | turtle  | Unique id of his identity token.                         |

## Session token

A session token is issued to the client on a [login request](resources/Session.md#login-create-new-sessionbr-session)
after it successfully identified itself and the identity has been verified.

They automatically become invalid once the time provided via the `exp` claim is reached and subsequent requests to
privileged API endpoints with the expired token will result in a `401 Unauthorized` response. Additionally, the API may
choose to invalidate a session token manually for security reasons. In that case the client application should request a
new session by using its identity token.

### JWT structure

#### Header

| Field | Type   | Description                                                                             |
|-------|--------|-----------------------------------------------------------------------------------------|
| typ   | string | Token type ("JWT")                                                                      |
| alg   | string | A hint to what algorithm should be used to verify the token.                            |
| kid   | turtle | Unique id of the [identity token](#identity-token) used to generate this session token. |

#### Payload

| Field | Type             | Description                                              |
|-------|------------------|----------------------------------------------------------|
| iss   | string           | Token issuer. Typically "Shelly".                        |
| sub   | turtle           | Token subject. This is the unique id of the application. |
| exp   | numDate          | A timestamp that this token must not be used _after_.    |
| ?nbf  | numDate          | A timestamp that this token must not be used _before_.   |
| jti   | turtle           | Unique id of his session token.                          |
| aud   | array of strings |                                                          |

The JWT also contains the implicit information about when it was issued. A timestamp can be extracted from the unique id
in the `jti` field, as turtle ids are time-based.
