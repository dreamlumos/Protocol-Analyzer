# LU3IN033-ProtocolAnalyzer
Projet de programmation d'un analyseur de protocoles réseau "offline" | UE : LU3IN033 Sorbonne Université

Structure du code :

* Package : protocolAnalyzer​
  * [Classe] Analyzer (main)​
  * [Classe] Trace​
  * [Interface] DataUnit​
  * [Classe] Ethernet *implements DataUnit*​
  * [Classe] IPV4 *implements DataUnit*​
  * [Classe] TCP *implements DataUnit*​
  * [Classe] HTTP *implements DataUnit*
​
* Package : tools​
  * [Classe] Traces​
  * [Classe] TraceFile​
  * [Classe] HexTools