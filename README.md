# Vigilance

A full Java inter-op library for Minecraft mod settings. Built using [Elementa](https://github.com/Sk1erLLC/Elementa), 
this library provides a simple and clean configuration menu and an easy-to-use setting declaration system.

## Dependency

In your repository block, add:
```groovy
maven {
    url = "https://repo.sk1er.club/repository/maven-public"
}
```
To use the latest builds, use the following dependency:

```groovy
implementation "gg.essential:vigilance-$mcVersion-$mcPlatform:$buildNumber"
```
<details><summary>Build Reference</summary>
    <table>
      <tbody>
        <tr>
          <th>mcVersion</th>
          <th>mcPlatform</th>
          <th>buildNumber</th>
        </tr>
          <tr>
          <td>1.18</td>
          <td>fabric</td>
          <td>
            <img alt="1.18-fabric" src="https://badges.modcore.net/badge/dynamic/xml?color=A97BFF&label=%20&query=%2Fmetadata%2Fversioning%2Flatest&url=https://repo.sk1er.club/repository/maven-releases/gg/essential/vigilance-1.18-fabric/maven-metadata.xml">
          </td>
        </tr>
        <tr>
          <td>1.17.1</td>
          <td>fabric</td>
          <td>
            <img alt="1.17.1-fabric" src="https://badges.modcore.net/badge/dynamic/xml?color=A97BFF&label=%20&query=%2Fmetadata%2Fversioning%2Flatest&url=https://repo.sk1er.club/repository/maven-releases/gg/essential/vigilance-1.17.1-fabric/maven-metadata.xml">
          </td>
        </tr>
        <tr>
          <td>1.12.2</td>
          <td>forge</td>
          <td>
            <img alt="1.12.2-forge" src="https://badges.modcore.net/badge/dynamic/xml?color=A97BFF&label=%20&query=%2Fmetadata%2Fversioning%2Flatest&url=https://repo.sk1er.club/repository/maven-releases/gg/essential/vigilance-1.12.2-forge/maven-metadata.xml">
          </td>
        </tr>
        <tr>
          <td>1.8.9</td>
          <td>forge</td>
          <td>
            <img alt="1.8.9-forge" src="https://badges.modcore.net/badge/dynamic/xml?color=A97BFF&label=%20&query=%2Fmetadata%2Fversioning%2Flatest&url=https://repo.sk1er.club/repository/maven-releases/gg/essential/vigilance-1.8.9-forge/maven-metadata.xml">
          </td>
        </tr>
      </tbody>
    </table>

</details>

## Examples

For examples of how to use Vigilance, refer to the example package within Vigilance for [annotation style](https://github.com/Sk1erLLC/Vigilance/blob/master/src/main/kotlin/gg/essential/vigilance/example/ExampleConfig.kt) 
and [DSL style](https://github.com/Sk1erLLC/Vigilance/blob/master/src/main/kotlin/gg/essential/vigilance/example/ExampleConfigDSL.kt)
