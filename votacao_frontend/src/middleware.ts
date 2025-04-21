import { NextRequest, NextResponse } from 'next/server';

const TOKEN_KEY = 'token';

export default function middleware(request: NextRequest) {
  const token = request.cookies.get(TOKEN_KEY)?.value;

  if (!token) {
    return NextResponse.redirect(new URL('/', request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/sessoes/:path*'],
};
