import handlers, os

name = "Go To WebPage"
__dir = os.path.dirname(os.path.realpath(__file__))

root = handlers.Index(os.path.join(__dir, 'goto.html'))
root.common = handlers.StaticFiles(os.path.join(__dir, '../../../common'))
root.images = handlers.StaticFiles(os.path.join(__dir, '../images'))
root.css = handlers.StaticFiles(os.path.join(__dir, '../css'))