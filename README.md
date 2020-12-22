# Protocol Analyzer
Projet de programmation d'un analyseur de protocoles réseau "offline" | UE : LU3IN033 Sorbonne Université

# Protocoles implémentés #

* Ethernet
* IPv4
* TCP
* HTTP

# Structure du code #

* Package : protocolAnalyzer​
  * [Class] Analyzer (main)​
  * [Class] Trace​
  * [Interface] DataUnit​
  * [Class] Ethernet *implements DataUnit*​
  * [Class] IPV4 *implements DataUnit*​
  * [Class] TCP *implements DataUnit*​
  * [Class] HTTP *implements DataUnit*
​
* Package : tools​
  * [Class] Traces​
  * [Class] TraceFile​
  * [Class] HexTools