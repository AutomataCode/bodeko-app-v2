"""Simulador de QA aislado: cuenta llamadas y permite simular una respuesta perdida."""
from pathlib import Path
from http.server import ThreadingHTTPServer
import importlib.util,io,json,sys,threading

if '--disposable' not in sys.argv: raise SystemExit('Usar --disposable solo para pruebas locales.')
path=Path(__file__).resolve().parents[1]/'culqi-mock/server.py'
spec=importlib.util.spec_from_file_location('mock',path);mock=importlib.util.module_from_spec(spec);spec.loader.exec_module(mock)
mock.log=lambda message: None
counts={'charges':0,'refunds':0}
guard=threading.Lock()
class Handler(mock.Handler):
    def do_GET(self):
        if self.path!='/qa/stats': return self.reply(404,{})
        with guard: result=dict(counts)
        result['approvedCharges']=len(mock.CHARGES)
        result['refundedCharges']=sum(1 for charge in mock.CHARGES.values() if charge['refunded']>0)
        self.reply(200,result)
    def do_POST(self):
        raw=self.rfile.read(int(self.headers.get('Content-Length','0')))
        try: body=json.loads(raw)
        except ValueError: return self.reply(400,{})
        with guard:
            if self.path=='/v2/charges': counts['charges']+=1
            if self.path=='/v2/refunds': counts['refunds']+=1
        if self.path=='/v2/charges' and body.get('source_id')=='tkn_qa_uncertain':
            # El proveedor simulado cobra, pero el backend no recibe una confirmación utilizable.
            body['source_id']=mock.OK_TOKEN
            mock.create_charge(body)
            return self.reply(503,{'object':'error','type':'api_error','user_message':'Respuesta perdida en QA'})
        self.rfile=io.BytesIO(raw)
        super().do_POST()
print('Simulador QA en 127.0.0.1:8210; no usa dinero ni servicios externos.',flush=True)
ThreadingHTTPServer(('127.0.0.1',8210),Handler).serve_forever()
