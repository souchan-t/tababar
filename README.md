# tababar #

## Build & Run ##

```sh
$ cd tababar
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

```text
/
/users
    /:id
        /trainings
            /all
            /reserved
            /done
        /account
    /search

/login

/trainings
    /search
    /select/:id

```