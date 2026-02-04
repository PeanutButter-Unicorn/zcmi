## ZCLI - the Zendesk CLI & Management Interface

A cli tool for zendesk for managing your zendesk support tickets and help center articles.

This project is under development and subject to change.

# Build Locally

> [!NOTE]
> Graalvm 21 is needed to install this, along with the toolset for your OS. See [Getting Started with Graalvm]

The executable for this project is compiled with oracle's graalvm. An important part of creating the native image with
the graalvm is allowing the [tracing agent] to its thing, which happens when you run tests prior to compiling.

With that in mind, refer to [testing] instructions. Call the following to compile the native image for your OS:

```shell
./gradlew clean test nativeCompile
```

If you're in a pinch you can simply call `./gradlew nativecompile`

# Testing

A sandbox Zendesk instance is needed to run the tests in zcmi. The [help center of the
sandbox needs to be enabled], otherwise no changes to the Zendesk instance are
needed for this testing.

### Environment Variables

| Variable Name      | Property             |
|--------------------|----------------------|
| `ZCMI_SANDBOX_URL` | fqdn for the sandbox |
| `ZCMI_ADMIN_EMAIL` | [Admin] email        |
| `ZCMI_ADMIN_TOKEN` | Token for the admin  |

[Admin]:https://support.zendesk.com/hc/en-us/articles/7043760141978-Understanding-roles-and-permissions-in-Zendesk-QA

[Getting Started with Graalvm]:https://www.graalvm.org/latest/getting-started/

[help center of the sandbox needs to be enabled]:https://support.zendesk.com/hc/en-us/articles/5702269234330-Enabling-and-activating-your-help-center

[testing]:README.md#testing

[tracing agent]:https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/