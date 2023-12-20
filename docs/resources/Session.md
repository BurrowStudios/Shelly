[http-get]: https://img.shields.io/badge/GET-505CDC
[http-post]: https://img.shields.io/badge/POST-23A559
[http-delete]: https://img.shields.io/badge/DELETE-A12828

# Session resource

After an application has successfully identified itself and the provided identity token was valid, it may request the
creation of a new session. Only session tokens may be used to access privileged resources, thus the existence of a
session is required for most interactions with the API.

> [!NOTE]
> A session does not strictly comply with the HTTP semantics standard, as it is not handled as a resource internally
> that could be exposed via API endpoints. Furthermore, to minimize state on the
> [interface layer](https://github.com/BurrowStudios/api-docs) of our architecture and maximize security, a session
> resource cannot be accessed externally after it has been created. The only exception to this is the
> [logout](#logout-delete-sessionbr-sessionssubjectidsessionid) endpoint.

## Session object

See [tokens](../Tokens.md) for more information on what sessions and subjects are and what role they occupy in
authentication and authorization.

##### Session structure

| Field | Type   | Description               |
|-------|--------|---------------------------|
| id    | turtle | Unique id of this session |

##### Subject structure

| Field | Type   | Description               |
|-------|--------|---------------------------|
| id    | turtle | Unique id of this subject |

## Login: Create new session</br>![http-post] /sessions/[{subject.id}]()

| Parameter | Type   | Description                                                     |
|-----------|--------|-----------------------------------------------------------------|
| identity  | string | The application's [identity token](../Tokens.md#identity-token) |

## Logout: Delete Session</br>![http-delete] /sessions/[{subject.id}]()/[{session.id}](#session-structure)
Requests that a session should be marked as expired.
Returns `204 Not Content` on success.

## Logout: Delete all Sessions</br>![http-delete] /sessions/[{subject.id}]()
> [!WARN]
> This will also invalidate all active sessions of this subject.

Requests that **all** sessions should be marked as expired.
Returns `204 No Content` on success.
