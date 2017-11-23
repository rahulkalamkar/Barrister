package com.singular.barrister.Util;

public class WebServiceError {
    private int mErrorCode;
    private int mErrorSubCode;
    private String mDescription;
    
    public WebServiceError( ) {
        this( WebServiceErrorCodes.UNKNOWN, WebServiceErrorSubCodes.NONE, null );
    }
    
    public WebServiceError( int errorCode ) {
        this( errorCode, WebServiceErrorSubCodes.NONE, null );
    }
    
    public WebServiceError( int errorCode, int errorSubCode ) {
        this( errorCode, errorSubCode, null );
    }
    
    public WebServiceError( int errorCode, String description ) {
        this( errorCode, WebServiceErrorSubCodes.NONE, description );
    }
    
    public WebServiceError( int errorCode, int errorSubCode, String description ) {
        super( );
        this.mErrorCode = errorCode;
        this.mErrorSubCode = errorSubCode;
        this.mDescription = description;
    }
    
    public int getErrorCode( ) {
        return mErrorCode;
    }
    
    public int getErrorSubCode( ) {
        return mErrorSubCode;
    }
    
    public String getDescription( ) {
        return mDescription;
    }
    
    public void setErrorCode( int errorCode ) {
        this.mErrorCode = errorCode;
    }
    
    public void setErrorSubCode( int errorSubCode ) {
        this.mErrorSubCode = errorSubCode;
    }
    
    public void setDescription( String description ) {
        this.mDescription = description;
    }
    
    @Override
    public String toString( ) {
        StringBuilder builder = new StringBuilder( );
        builder.append( "WebServiceError { mErrorCode=" );
        builder.append( mErrorCode );
        builder.append( " mErrorSubCode=" );
        builder.append( mErrorSubCode );
        builder.append( " mDescription=" );
        builder.append( mDescription );
        builder.append( " }" );
        return builder.toString( );
    }
}
