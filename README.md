![KitXLogo](../main/KitXLogo.png)
# KitX
![Discord](https://discordapp.com/api/guilds/1133528601531252766/widget.png?style=shield)

A light weight kit manager plugin for spigot servers

### Suported Versions
1.8 through 1.18

### Commands
Commands | Description                                                    
-------------- |--------------------------------------------------------
`/kitx create <kitname>` | To create a kit with a specific name
`/kitx edit <kitname> cooldown <value>` | To set a cooldown for how long a kit. Use suffixes `s`, `m`, `h`, and `d` to specify seconds, minutes, hours and days.
`/kitx edit <kitname> limit <value>` | To set the limit for how many times a kit can be used
`/kitx edit <kitname> permission <value>` | To set a permission for a kit
`/kitx delete <kitname>`   | To delete a kit by it's name                                   
`/kitx list` | To list all the servers kits
`/kitx help` | To display the help section of the /kitx command               
`/kit <kitname>` | To get a certain kit. It will check the permission and cooldown 

### Permissions
Permission | Description 
--- | ---
`kitx.admin` | To use the `/kitx` command
`kit.cooldown.<kitname>.bypass` | To bypass a certain kit's cooldown
`kit.limit.<kitname>.bypass` | To bypass a certain kit's limit

### Support
You can get support with the plugin in our [discord](https://discord.gg/DwQHaky3Nf)

### Developer API
Currently the developer api isn't published. You can interact with the kits and database through it but you must build the plugin and publish the api to local. If someone actually uses the developer api open an issue and I will start publishing it to my repo.

### Credits
- [CyberedCake](https://github.com/CyberedCake) - The logo was remade by CyberedCake