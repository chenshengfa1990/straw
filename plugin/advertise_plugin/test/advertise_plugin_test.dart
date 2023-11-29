import 'package:flutter_test/flutter_test.dart';
import 'package:advertise_plugin/advertise_plugin.dart';
import 'package:advertise_plugin/advertise_plugin_platform_interface.dart';
import 'package:advertise_plugin/advertise_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockAdvertisePluginPlatform
    with MockPlatformInterfaceMixin
    implements AdvertisePluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final AdvertisePluginPlatform initialPlatform = AdvertisePluginPlatform.instance;

  test('$MethodChannelAdvertisePlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelAdvertisePlugin>());
  });

  test('getPlatformVersion', () async {
    AdvertisePlugin advertisePlugin = AdvertisePlugin();
    MockAdvertisePluginPlatform fakePlatform = MockAdvertisePluginPlatform();
    AdvertisePluginPlatform.instance = fakePlatform;

    expect(await advertisePlugin.getPlatformVersion(), '42');
  });
}
