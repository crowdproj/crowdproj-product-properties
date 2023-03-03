# Module `product-properties-common`

A module contains only objects that are used by all other modules in the project. **Forbidden** to add here
dependencies that are specific to some specific implementation (Postgres, WebsocketSession).
The number of dependencies for this module should be reduced.
It is allowed to add dependencies to basic libraries like kotlinx.datetime, kotlinx.coroutines and the like.