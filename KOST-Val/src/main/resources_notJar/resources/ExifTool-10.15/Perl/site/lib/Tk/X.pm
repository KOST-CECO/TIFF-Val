package Tk::X;

use strict;
use Carp;
use vars qw($VERSION @EXPORT $AUTOLOAD);
$VERSION = '4.004'; # $Id: //depot/Tkutf8/Xlib/X/X.pm#4 $
use Tk qw($XS_VERSION);

require Exporter;
require DynaLoader;
require AutoLoader;


use base  qw(Exporter DynaLoader);
# Items to export into callers namespace by default. Note: do not export
# names by default without a very good reason. Use EXPORT_OK instead.
# Do not simply export all your public functions/methods/constants.
@EXPORT = qw(
	Above
	AllTemporary
	AllocAll
	AllocNone
	AllowExposures
	AlreadyGrabbed
	Always
	AnyButton
	AnyKey
	AnyModifier
	AnyPropertyType
	ArcChord
	ArcPieSlice
	AsyncBoth
	AsyncKeyboard
	AsyncPointer
	AutoRepeatModeDefault
	AutoRepeatModeOff
	AutoRepeatModeOn
	BadAccess
	BadAlloc
	BadAtom
	BadColor
	BadCursor
	BadDrawable
	BadFont
	BadGC
	BadIDChoice
	BadImplementation
	BadLength
	BadMatch
	BadName
	BadPixmap
	BadRequest
	BadValue
	BadWindow
	Below
	BottomIf
	Button1
	Button1Mask
	Button1MotionMask
	Button2
	Button2Mask
	Button2MotionMask
	Button3
	Button3Mask
	Button3MotionMask
	Button4
	Button4Mask
	Button4MotionMask
	Button5
	Button5Mask
	Button5MotionMask
	ButtonMotionMask
	ButtonPress
	ButtonPressMask
	ButtonRelease
	ButtonReleaseMask
	CWBackPixel
	CWBackPixmap
	CWBackingPixel
	CWBackingPlanes
	CWBackingStore
	CWBitGravity
	CWBorderPixel
	CWBorderPixmap
	CWBorderWidth
	CWColormap
	CWCursor
	CWDontPropagate
	CWEventMask
	CWHeight
	CWOverrideRedirect
	CWSaveUnder
	CWSibling
	CWStackMode
	CWWidth
	CWWinGravity
	CWX
	CWY
	CapButt
	CapNotLast
	CapProjecting
	CapRound
	CenterGravity
	CirculateNotify
	CirculateRequest
	ClientMessage
	ClipByChildren
	ColormapChangeMask
	ColormapInstalled
	ColormapNotify
	ColormapUninstalled
	Complex
	ConfigureNotify
	ConfigureRequest
	ControlMapIndex
	ControlMask
	Convex
	CoordModeOrigin
	CoordModePrevious
	CopyFromParent
	CreateNotify
	CurrentTime
	CursorShape
	DefaultBlanking
	DefaultExposures
	DestroyAll
	DestroyNotify
	DirectColor
	DisableAccess
	DisableScreenInterval
	DisableScreenSaver
	DoBlue
	DoGreen
	DoRed
	DontAllowExposures
	DontPreferBlanking
	EastGravity
	EnableAccess
	EnterNotify
	EnterWindowMask
	EvenOddRule
	Expose
	ExposureMask
	FamilyChaos
	FamilyDECnet
	FamilyInternet
	FillOpaqueStippled
	FillSolid
	FillStippled
	FillTiled
	FirstExtensionError
	FocusChangeMask
	FocusIn
	FocusOut
	FontChange
	FontLeftToRight
	FontRightToLeft
	ForgetGravity
	GCArcMode
	GCBackground
	GCCapStyle
	GCClipMask
	GCClipXOrigin
	GCClipYOrigin
	GCDashList
	GCDashOffset
	GCFillRule
	GCFillStyle
	GCFont
	GCForeground
	GCFunction
	GCGraphicsExposures
	GCJoinStyle
	GCLastBit
	GCLineStyle
	GCLineWidth
	GCPlaneMask
	GCStipple
	GCSubwindowMode
	GCTile
	GCTileStipXOrigin
	GCTileStipYOrigin
	GXand
	GXandInverted
	GXandReverse
	GXclear
	GXcopy
	GXcopyInverted
	GXequiv
	GXinvert
	GXnand
	GXnoop
	GXnor
	GXor
	GXorInverted
	GXorReverse
	GXset
	GXxor
	GrabFrozen
	GrabInvalidTime
	GrabModeAsync
	GrabModeSync
	GrabNotViewable
	GrabSuccess
	GraphicsExpose
	GravityNotify
	GrayScale
	HostDelete
	HostInsert
	IncludeInferiors
	InputFocus
	InputOnly
	InputOutput
	IsUnmapped
	IsUnviewable
	IsViewable
	JoinBevel
	JoinMiter
	JoinRound
	KBAutoRepeatMode
	KBBellDuration
	KBBellPercent
	KBBellPitch
	KBKey
	KBKeyClickPercent
	KBLed
	KBLedMode
	KeyPress
	KeyPressMask
	KeyRelease
	KeyReleaseMask
	KeymapNotify
	KeymapStateMask
	LASTEvent
	LSBFirst
	LastExtensionError
	LeaveNotify
	LeaveWindowMask
	LedModeOff
	LedModeOn
	LineDoubleDash
	LineOnOffDash
	LineSolid
	LockMapIndex
	LockMask
	LowerHighest
	MSBFirst
	MapNotify
	MapRequest
	MappingBusy
	MappingFailed
	MappingKeyboard
	MappingModifier
	MappingNotify
	MappingPointer
	MappingSuccess
	Mod1MapIndex
	Mod1Mask
	Mod2MapIndex
	Mod2Mask
	Mod3MapIndex
	Mod3Mask
	Mod4MapIndex
	Mod4Mask
	Mod5MapIndex
	Mod5Mask
	MotionNotify
	NoEventMask
	NoExpose
	NoSymbol
	Nonconvex
	None
	NorthEastGravity
	NorthGravity
	NorthWestGravity
	NotUseful
	NotifyAncestor
	NotifyDetailNone
	NotifyGrab
	NotifyHint
	NotifyInferior
	NotifyNonlinear
	NotifyNonlinearVirtual
	NotifyNormal
	NotifyPointer
	NotifyPointerRoot
	NotifyUngrab
	NotifyVirtual
	NotifyWhileGrabbed
	Opposite
	OwnerGrabButtonMask
	ParentRelative
	PlaceOnBottom
	PlaceOnTop
	PointerMotionHintMask
	PointerMotionMask
	PointerRoot
	PointerWindow
	PreferBlanking
	PropModeAppend
	PropModePrepend
	PropModeReplace
	PropertyChangeMask
	PropertyDelete
	PropertyNewValue
	PropertyNotify
	PseudoColor
	RaiseLowest
	ReparentNotify
	ReplayKeyboard
	ReplayPointer
	ResizeRedirectMask
	ResizeRequest
	RetainPermanent
	RetainTemporary
	RevertToNone
	RevertToParent
	RevertToPointerRoot
	ScreenSaverActive
	ScreenSaverReset
	SelectionClear
	SelectionNotify
	SelectionRequest
	SetModeDelete
	SetModeInsert
	ShiftMapIndex
	ShiftMask
	SouthEastGravity
	SouthGravity
	SouthWestGravity
	StaticColor
	StaticGravity
	StaticGray
	StippleShape
	StructureNotifyMask
	SubstructureNotifyMask
	SubstructureRedirectMask
	Success
	SyncBoth
	SyncKeyboard
	SyncPointer
	TileShape
	TopIf
	TrueColor
	UnmapGravity
	UnmapNotify
	Unsorted
	VisibilityChangeMask
	VisibilityFullyObscured
	VisibilityNotify
	VisibilityPartiallyObscured
	VisibilityUnobscured
	WestGravity
	WhenMapped
	WindingRule
	XYBitmap
	XYPixmap
	X_H
	X_PROTOCOL
	X_PROTOCOL_REVISION
	YSorted
	YXBanded
	YXSorted
	ZPixmap
);

sub AUTOLOAD {
    # This AUTOLOAD is used to 'autoload' constants from the constant()
    # XS function.  If a constant is not found then control is passed
    # to the AUTOLOAD in AutoLoader.

    my $constname;
    ($constname = $AUTOLOAD) =~ s/.*:://;
    my $val = constant($constname, @_ ? $_[0] : 0);
    if ($! != 0) {
	if ($! =~ /Invalid/) {
	    $AutoLoader::AUTOLOAD = $AUTOLOAD;
	    goto &AutoLoader::AUTOLOAD;
	}
	else {
		croak "Your vendor has not defined X macro $constname";
	}
    }
    eval "sub $AUTOLOAD { $val }";
    goto &$AUTOLOAD;
}

bootstrap Tk::X;

# Preloaded methods go here.

# Autoload methods go after =cut, and are processed by the autosplit program.

1;
__END__
# Below is the stub of documentation for your module. You better edit it!

=cut
