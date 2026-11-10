# Simulador Culqi del equipo

`server.py` es el archivo proporcionado por tus compañeros, copiado sin modificaciones desde el adjunto.

Desde la raíz del repositorio:

```powershell
python .\scripts\culqi-mock\server.py
```

Mantén esa terminal abierta. En `.env.dev`, en la raíz del backend:

```dotenv
CULQI_API_URL=http://127.0.0.1:8199/v2
CULQI_SECRET_KEY=sk_test_mock
```

Reinicia el backend desde la raíz del proyecto para que cargue el archivo. Si IntelliJ o tu terminal tienen variables `CULQI_API_URL` o `CULQI_SECRET_KEY` del simulador anterior, elimínalas o actualízalas: las variables del proceso tienen prioridad sobre este archivo.

El frontend usa `VITE_PAYMENT_MODE=token` (es también el modo predeterminado). En la pantalla de pago:

| Token | Resultado |
| --- | --- |
| `tkn_test_ok` | Pago aprobado |
| `tkn_test_declined` | Pago rechazado |

El simulador no mueve dinero ni contacta a Culqi. Conserva los cargos en memoria y valida los reembolsos: una orden debe haber sido pagada en esa misma ejecución del simulador para poder devolverla. Los pagos históricos del SQL demo y los realizados con el simulador anterior no existen en su memoria. Para probar una devolución, crea una orden nueva, págala con `tkn_test_ok` y cancélala como administrador sin reiniciar el simulador.

La prueba `frontend/tools/verify-api.mjs` usa `tkn_test_ok` por defecto. El simulador anterior sigue disponible en `frontend/tools/culqi-simulator.py`, pero usa otro puerto, otra clave y otro token; no es el configurado en `.env.dev`. Para ejecutar la prueba contra aquel, configura `QA_APPROVED_TOKEN=tkn_test_approved` además de sus valores de backend.
