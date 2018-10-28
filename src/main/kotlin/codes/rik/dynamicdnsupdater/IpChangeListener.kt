package codes.rik.dynamicdnsupdater

interface IpChangeListener {
    fun onIpChange(ipAddress: IpAddress)
}