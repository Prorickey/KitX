![KitXLogo](../main/KitXLogo.png)
# KitX
![Discord](https://discordapp.com/api/guilds/924043990465146931/widget.png?style=shield)

A light weight kit manager plugin for spigot servers

### Suported Versions
1.8 through 1.18

### Commands
Commands | Description                                                    
-------------- |--------------------------------------------------------
`/kitx create <kitname> [cooldown] [limit] [permission]` | To create a kit with an optional cooldown and or permission. In the cooldown you can put `s`, `m`, `h` and `d` after the number to convert it to seconds, minutes, hours and days. Having no suffix will default to seconds. If you do not want a limit, put it as 0 or leave it blank. 
`/kitx delete <kitname>`   | To delete a kit by it's name                                   
`/kitx help` | To display the help section of the /kitx command               
`/kit <kitname>` | To get a certain kit. It will check the permission and cooldown 

### Permissions
Permission | Description 
--- | ---
`kitx.subcommands.create` | To create a kit with the /kitx create command
`kitx.subcommands.delete` | To delete a kit with the /kitx delete command
`kitx.subcommands.list` | To list all the kits with the /kitx list command
`kit.cooldown.<kitname>.bypass` | To bypass a certain kit's cooldown
`kit.limit.<kitname>.bypass` | To bypass a certain kit's limit

### Support
You can get support with the plugin in our [discord](https://discord.gg/DwQHaky3Nf)

### Credits
- [CyberedCake](https://github.com/CyberedCake) - The logo was remade by CyberedCake