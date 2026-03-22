# Security Analysis: Detected input from a HTTPServletRequest going into a 'Proce

**Tool:** semgrep | **Language:** java
**Findings:** 2

## Affected Locations

- `src\main\java\org\t246osslab\easybuggy\troubles\EndlessWaitingServlet.java` line 53: Detected input from a HTTPServletRequest going into a 'ProcessBuilder' or 'exec'
- `src\main\java\org\t246osslab\easybuggy\troubles\EndlessWaitingServlet.java` line 54: Detected input from a HTTPServletRequest going into a 'ProcessBuilder' or 'exec'
