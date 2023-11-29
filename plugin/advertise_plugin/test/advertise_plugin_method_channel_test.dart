import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:advertise_plugin/advertise_plugin_method_channel.dart';

void main() {
  MethodChannelAdvertisePlugin platform = MethodChannelAdvertisePlugin();
  const MethodChannel channel = MethodChannel('advertise_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
