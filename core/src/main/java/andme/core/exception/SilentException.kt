package andme.core.exception

class SilentException : Exception {
    constructor() : super() {}
    constructor(msg: String?) : super(msg) {}
    constructor(detailMessage: String?, throwable: Throwable?) : super(detailMessage, throwable) {}
}