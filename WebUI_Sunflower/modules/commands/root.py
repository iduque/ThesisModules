import handlers, os#

name = "Commands WebPage"
__dir = os.path.dirname(os.path.realpath(__file__))

root = handlers.Index(os.path.join(__dir, 'commands.html'))
root.common = handlers.StaticFiles(os.path.join(__dir, '../../common'))
root.images = handlers.StaticFiles(os.path.join(__dir, 'images'))
root.css = handlers.StaticFiles(os.path.join(__dir, 'css'))

from modules.commands.goto import root as goTo
root.goto = goTo

from modules.commands.transportto import root as transportTo
root.transportto = transportTo

from modules.commands.admin import root as admin
root.admin = admin