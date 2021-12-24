# KitX
A light weight kit manager plugin for spigot servers

### Commands
Commands | Description 
--- | ---
/kitx create <kitname> [cooldown] [permission] | To create a kit with an optional cooldown and or permission
/kitx delete <kitname> | To delete a kit by it's name
/kitx help | To display the help section of the /kitx command
/kit <kitname> | To get a certain kit. It will check the permission and cooldown

### Permissions
Permission | Description 
--- | ---
kitx.subcommands.create | To create a kit with the /kitx create command
kitx.subcommands.delete | To delete a kit with the /kitx delete command
kitx.subcommands.list | To list all the kits with the /kitx list command
kit.cooldown.<kitname>.bypass | To bypass a certain kits cooldown