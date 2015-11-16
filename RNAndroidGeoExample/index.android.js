/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';

var React = require('react-native');
var AndroidGeolocation = require('react-native-android-geolocation')

var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} = React;

var RNAndroidGeoExample = React.createClass({
  getInitialState: function() {
    AndroidGeolocation.getCurrentLocation(
      (position) => this.setState({position: position}),
      (error) => this.setState({error: error})
    );
    return {
      position: {
        coords: {
          lat: 'Unknown',
          lng: 'Unknown'
        }
      },
      error: ''
    };
  },
  render: function() {
    var content;
    if( this.state.error === '' ) {
      content = (
        <Text style={styles.text}>
          {'You are at: \nLat: ' + this.state.position.coords.latitude + '\nLng: ' + this.state.position.coords.longitude}
        </Text>
      );
    } else {
      content = (
        <Text style={styles.text}>
          {this.state.error}
        </Text>
      );
    }
    return (
      <View style={styles.container}>
        {content}
      </View>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  text: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('RNAndroidGeoExample', () => RNAndroidGeoExample);
