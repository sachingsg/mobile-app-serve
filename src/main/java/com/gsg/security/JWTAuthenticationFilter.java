package com.gsg.security;

public class JWTAuthenticationFilter  {/*
	
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl("/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {

			AppUser user = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							user.getEmail(), 
							user.getPassword(), 
							new ArrayList<>())
					);
		} catch (IOException ioe) {
			throw new RuntimeException();
		}

	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

			String token = Jwts.builder().setSubject(((User)auth.getPrincipal()).getUsername())
			.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
            .compact();
			
		response.addHeader(HEADER_STRING, TOKEN_PREFIX +" "+ token);
	}

*/}
