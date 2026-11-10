"""Pruebas del detector con valores ficticios, sin leer credenciales externas."""
import importlib.util
import json
import unittest
from pathlib import Path

spec = importlib.util.spec_from_file_location('detector', Path(__file__).with_name('verificar-secretos.py'))
detector = importlib.util.module_from_spec(spec)
spec.loader.exec_module(detector)


class DetectorTests(unittest.TestCase):
    """Comprueba detección, exclusiones permitidas y ocultamiento de valores."""

    def test_password_property(self):
        result = detector.inspect('application.properties', b'spring.datasource.password=valor-ficticio', 'prueba')
        self.assertTrue(result)
        self.assertNotIn('valor-ficticio', json.dumps(result))

    def test_environment_reference(self):
        self.assertEqual(detector.inspect('application.properties', b'jwt.secret=${JWT_SECRET}', 'prueba'), [])

    def test_environment_with_secret_fallback(self):
        sample = b'jwt.secret=${JWT_SECRET:valor-ficticio}'
        self.assertTrue(detector.inspect('application.properties', sample, 'prueba'))

    def test_typescript_type(self):
        self.assertEqual(detector.inspect('types.ts', b'password: string;', 'prueba'), [])

    def test_private_environment(self):
        self.assertTrue(detector.private_path('frontend/.env.local'))
        self.assertTrue(detector.private_path('.env.dev'))

    def test_public_example(self):
        self.assertFalse(detector.private_path('.env.example'))
        self.assertFalse(detector.private_path('frontend/.env.example'))

    def test_backup_and_certificate(self):
        self.assertTrue(detector.private_path('datos/base.dump'))
        self.assertTrue(detector.private_path('claves/certificado.pem'))

    def test_qa_data(self):
        self.assertTrue(detector.private_path('frontend/.qa/pg-landing/PG_VERSION'))

    def test_simulator_value(self):
        text = ('CULQI_SECRET_KEY=' + 'sk_test_mock').encode()
        self.assertEqual(detector.inspect('ejemplo.txt', text, 'prueba'), [])

    def test_provider_key_in_comment(self):
        sample = ('# token: ' + 'sk_' + 'live_' + 'valorFicticio1234567890').encode()
        self.assertTrue(detector.inspect('example.ts', sample, 'prueba'))

    def test_database_url_with_password(self):
        sample = ('postgresql://' + 'usuario:valor-ficticio@localhost/base').encode()
        self.assertTrue(detector.inspect('config.txt', sample, 'prueba'))


if __name__ == '__main__':
    unittest.main()
